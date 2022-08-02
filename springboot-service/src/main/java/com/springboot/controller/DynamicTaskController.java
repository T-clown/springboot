package com.springboot.controller;

import com.springboot.task.DynamicTaskService;
import com.springboot.task.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
@Slf4j
@RestController
@RequestMapping("/dynamic/task")
public class DynamicTaskController {

    private final DynamicTaskService dynamicTaskService;

    public DynamicTaskController(DynamicTaskService dynamicTaskService) {
        this.dynamicTaskService = dynamicTaskService;
    }

    /**
     * 查看已开启但还未执行的动态任务
     * @return
     */
    @GetMapping
    public List<String> getStartingDynamicTask(){
        return dynamicTaskService.getTasks();
    }


    /**
     * 开启一个动态任务
     * @param task
     * @return
     */
    @PostMapping("/dynamic")
    public String startDynamicTask(){
        Task task = Task.builder().executeTime(LocalDateTime.now().plusSeconds(10)).name("测试").runnable(() -> test("测试")).build();
        // 将这个添加到动态定时任务中去
        dynamicTaskService.add(task);
        return "动态任务:"+task.getName()+" 已开启";
    }
    private void test(String name){
        log.info("定时任务执行:{}",name);
    }



    /**
     *  根据名称 停止一个动态任务
     * @param name
     * @return
     */
    @DeleteMapping("/{name}")
    public String stopDynamicTask(@PathVariable("name") String name){
        // 将这个添加到动态定时任务中去
        if(!dynamicTaskService.stop(name)){
            return "停止失败,任务已在进行中.";
        }
        return "任务已停止";
    }

}

