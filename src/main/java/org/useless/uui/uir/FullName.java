package org.useless.uui.uir;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.MODULE;
import static java.lang.annotation.ElementType.PACKAGE;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;

/**
 * <h6>完全名</h6>
 * 用以展示方法完全名称
 * @apiNote 用以显示方法简略后的完整名称
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(value={CONSTRUCTOR, FIELD, LOCAL_VARIABLE, METHOD, PACKAGE, MODULE, PARAMETER, TYPE})
public @interface FullName {
    /**
     * 完全名<br>
     * 用于展示缩写方法的玩整名称
     * @return 方法完整名称
     */
    String fullName();
}
