import java.io.File;
import java.io.IOException;

import org.mybatis.generator.api.ShellRunner;

/**
 * Created by lixuejiao on 16/7/26.
 */
public class GeneratorMain {
    public static void main(String[] args) {
        args = new String[3];
        args[0] = "-configfile";
        GeneratorMain.class.getResource("/");
        String configRootPath = GeneratorMain.class.getResource("/").getPath();
        args[1] = configRootPath + "generator.xml";
        args[2] = "-overwrite";
        deleteJavaFiles();
        deleteXmlFiles();
        ShellRunner.main(args);
    }

    public static void deleteJavaFiles() {
        try {
            File directory = new File("");
            String str = directory.getCanonicalPath() + "/springboot-dao/src/main/java/com/springboot/dao/dto";
            File file = new File(str);
            if (file.isDirectory() && null != file.listFiles() && file.listFiles().length > 0) {
                for (File f : file.listFiles()) {
                    f.delete();
                }
            }
            directory = new File("");
            str = directory.getCanonicalPath() + "/springboot-dao/src/main/java/com/springboot/dao/generatedMapper";
            file = new File(str);
            if (file.isDirectory() && null != file.listFiles() && file.listFiles().length > 0) {
                for (File f : file.listFiles()) {
                    f.delete();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteXmlFiles() {
        try {
            File directory = new File("");
            String str = directory.getCanonicalPath() + "/springboot-dao/src/main/resources/com/springboot/dao/generatedMapper";
            File file = new File(str);
            if (file.isDirectory() && null != file.listFiles() && file.listFiles().length > 0) {
                for (File f : file.listFiles()) {
                    f.delete();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
