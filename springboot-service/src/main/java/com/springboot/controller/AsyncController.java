package com.springboot.controller;

import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;

import jakarta.servlet.AsyncContext;
import jakarta.servlet.AsyncEvent;
import jakarta.servlet.AsyncListener;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.context.request.async.TimeoutCallableProcessingInterceptor;
import org.springframework.web.context.request.async.WebAsyncTask;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * https://www.cnblogs.com/baixianlong/p/10661591.html
 */
@Tag(name = "异步执行测试", description = "异步执行测试")
@Slf4j
@RestController
public class AsyncController {

    public static final ThreadPoolExecutor EXECUTOR =
        new ThreadPoolExecutor(15, 15, 0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(), new ThreadFactory() {
            private final AtomicInteger threadNumber = new AtomicInteger(1);
            private static final String NAME_PREFIX = "Thread";

            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(Thread.currentThread().getThreadGroup(), r,
                    NAME_PREFIX + threadNumber.getAndIncrement(),
                    0);
                //是否守护进程
                if (t.isDaemon()) {
                    t.setDaemon(false);
                }
                if (t.getPriority() != Thread.NORM_PRIORITY) {
                    t.setPriority(Thread.NORM_PRIORITY);
                }
                return t;
            }
        }, new AbortPolicy());

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(EXECUTOR::shutdown));
    }

    @RequestMapping(value = "/email/servletReq", method = GET)
    public void servletReq(HttpServletRequest request, HttpServletResponse response) {
        AsyncContext asyncContext = request.startAsync();
        //设置监听器:可设置其开始、完成、异常、超时等事件的回调处理
        asyncContext.addListener(new AsyncListener() {
            @Override
            public void onTimeout(AsyncEvent event) {
                log.warn("servletReq超时了...");
                //做一些超时后的相关操作...
            }

            @Override
            public void onStartAsync(AsyncEvent event) {
                log.info("线程开始");
            }

            @Override
            public void onError(AsyncEvent event) {
                log.error("发生错误：" + event.getThrowable());
            }

            @Override
            public void onComplete(AsyncEvent event) {
                log.info("执行完成");
                //这里可以做一些清理资源的操作...
            }
        });
        //设置超时时间
        asyncContext.setTimeout(20000);
        asyncContext.start(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                    log.info("内部线程：" + Thread.currentThread().getName());
                    asyncContext.getResponse().setCharacterEncoding("utf-8");
                    asyncContext.getResponse().setContentType("text/html;charset=UTF-8");
                    asyncContext.getResponse().getWriter().println("这是异步的请求返回");
                } catch (Exception e) {
                    log.info("异常：" + e);
                }
                //异步请求完成通知
                //此时整个请求才完成
                asyncContext.complete();
            }
        });
        //此时之类 request的线程连接已经释放了
        log.info("主线程：" + Thread.currentThread().getName());
    }

    @RequestMapping(value = "/email/callableReq", method = GET)
    public Callable<String> callableReq() {
        log.info("外部线程：" + Thread.currentThread().getName());

        return () -> { Thread.sleep(10000);
            log.info("内部线程：" + Thread.currentThread().getName());
            return "callable!";
        };
    }

    @Configuration
    public class RequestAsyncPoolConfig implements WebMvcConfigurer {

        @Resource
        private ThreadPoolTaskExecutor asyncTaskExecutor;

        @Override
        public void configureAsyncSupport(final AsyncSupportConfigurer configurer) {
            //处理 callable超时
            configurer.setDefaultTimeout(60 * 1000);
            configurer.setTaskExecutor(asyncTaskExecutor);
            configurer.registerCallableInterceptors(timeoutCallableProcessingInterceptor());
        }

        @Bean
        public TimeoutCallableProcessingInterceptor timeoutCallableProcessingInterceptor() {
            return new TimeoutCallableProcessingInterceptor();
        }
    }

    @RequestMapping(value = "/email/webAsyncReq", method = GET)
    public WebAsyncTask<String> webAsyncReq () {
        log.info("外部线程：" + Thread.currentThread().getName());
        Callable<String> result = () -> {
            log.info("内部线程开始：" + Thread.currentThread().getName());
            try {
                TimeUnit.SECONDS.sleep(4);
            } catch (Exception e) {
                // TODO: handle exception
            }
            log.info("副线程返回");
            log.info("内部线程返回：" + Thread.currentThread().getName());
            return "success";
        };
        WebAsyncTask<String> wat = new WebAsyncTask<String>(3000L, result);
        wat.onTimeout(() -> {
            // TODO Auto-generated method stub
            return "超时";
        });
        return wat;
    }

    @RequestMapping(value = "/email/deferredResultReq", method = GET)
    public DeferredResult<String> deferredResultReq () {
        log.info("外部线程：" + Thread.currentThread().getName());
        //设置超时时间
        DeferredResult<String> result = new DeferredResult<>(6 * 1000L);
        //处理超时事件 采用委托机制
        result.onTimeout(() -> {
            log.warn("DeferredResult超时");
            result.setResult("超时了!");
        });
        result.onCompletion(() -> {
            //完成后
            log.info("调用完成");
        });
        EXECUTOR.execute(() -> {
            //处理业务逻辑
            log.info("内部线程：" + Thread.currentThread().getName());
            //返回结果
            result.setResult("DeferredResult!!");
        });
        return result;
    }
}
