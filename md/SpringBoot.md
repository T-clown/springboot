#@ImportResource用于导入Spring的配置文件，让配置文件（如applicationContext.xml）里面的内容生效
#1.spring的配置文件目录可以放在
    /config
    /(根目录)
    resource/config/
    resource/
    这四个路径从上到下存在优先级关系
#2.SpringBoot的注解扫描的默认规则是SpringBoot的入口类所在包及其子包