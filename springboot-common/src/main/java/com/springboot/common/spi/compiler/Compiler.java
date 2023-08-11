
package com.springboot.common.spi.compiler;


import com.springboot.common.spi.annotation.SPI;
import com.springboot.common.spi.compiler.support.JavassistCompiler;

/**
 * Compiler. (SPI, Singleton, ThreadSafe)
 */
@SPI(JavassistCompiler.NAME)
public interface Compiler {

    /**
     * Compile java source code.
     *
     * @param code        Java source code
     * @param classLoader classloader
     * @return Compiled class
     */
    Class<?> compile(String code, ClassLoader classLoader);

}
