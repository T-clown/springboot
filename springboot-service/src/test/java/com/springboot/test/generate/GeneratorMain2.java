package com.springboot.test.generate;

import org.mybatis.generator.api.ShellRunner;


public class GeneratorMain2 {
    public static void main(String[] args) {
        args = new String[3];
        args[0] = "-configfile";
        GeneratorMain2.class.getResource("/");
        String configRootPath = GeneratorMain2.class.getResource("/").getPath();
        args[1] = configRootPath + "generator2.xml";
        args[2] = "-overwrite";
        ShellRunner.main(args);
    }
}
