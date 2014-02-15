package bzh.terrevirtuelle.navisu.api.option.annotation;

import bzh.terrevirtuelle.navisu.api.option.mapping.IntMapper;
import bzh.terrevirtuelle.navisu.api.option.mapping.StringMapper;

import static java.lang.annotation.ElementType.FIELD;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;

/**
 * NaVisu
 *
 * @author tibus
 * @date 15/02/2014 18:26
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface IntOption {

    int value() default 0;

    Class<? extends StringMapper> mapper() default IntMapper.class;
}
