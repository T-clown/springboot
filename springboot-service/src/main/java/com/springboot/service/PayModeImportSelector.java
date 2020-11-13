package com.springboot.service;

import java.lang.annotation.Annotation;

import com.springboot.common.enums.CommonYN;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;

public abstract class PayModeImportSelector<A extends Annotation> implements ImportSelector {

    @Override
    public final String[] selectImports(AnnotationMetadata annotationMetadata) {
        Class<?> annType = GenericTypeResolver.resolveTypeArgument(getClass(), PayModeImportSelector.class);
        Assert.state(annType != null, "Unresolvable type argument for AdviceModeImportSelector");
        MergedAnnotations annotations = annotationMetadata.getAnnotations();
        MergedAnnotation<Annotation> mergedAnnotation = annotations.get(annType.getName());
        CommonYN mode = mergedAnnotation.getEnum("mode", CommonYN.class);
        String[] imports = selectImports(mode);
        if (imports == null) {
            throw new IllegalArgumentException("Unknown AdviceMode: " + mode);
        }
        return imports;
    }

    public abstract String[] selectImports(CommonYN commonYN);
}
