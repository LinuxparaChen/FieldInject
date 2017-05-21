package com.chenzhanyang;

import com.google.auto.service.AutoService;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

/**
 * Description:
 * Auther: chen_zhanyang.
 * Email: zhanyang.chen@gmail.com
 * Data: 2017/5/19.
 */

@AutoService(Processor.class)
public class FieldInjectProcessor extends AbstractProcessor {
    /**
     * 元素相关的辅助类
     */
    private Elements mElementUtils;
    /**
     * 日志相关的辅助类
     */
    private Messager mMessager;
    /**
     * 文件相关的辅助类
     */
    private Filer mFiler;


    private Map<String, GenerateJavaFile> mJavaFileMap = new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mElementUtils = processingEnvironment.getElementUtils();
        mMessager = processingEnvironment.getMessager();
        mFiler = processingEnvironment.getFiler();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new HashSet<>();
        annotations.add(FieldBind.class.getCanonicalName());
        return annotations;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnv) {
        processFieldBind(roundEnv);
        for (Map.Entry<String,GenerateJavaFile> entry : mJavaFileMap.entrySet()) {
            GenerateJavaFile generateJavaFile = entry.getValue();
            try {
                generateJavaFile.buildJavaFile().writeTo(mFiler);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 处理字段绑定注解
     *
     * @param roundEnv
     */
    private void processFieldBind(RoundEnvironment roundEnv) {
        for (Element fieldElement : roundEnv.getElementsAnnotatedWith(FieldBind.class)) {
            String fieldType = fieldElement.getAnnotation(FieldBind.class).value();
            if (fieldType.equals("")){
                fieldType = fieldElement.asType().toString();
            }
            TypeElement classElement = (TypeElement) fieldElement.getEnclosingElement();
            saveGenerateJavaFile(classElement, fieldElement,fieldType);
        }
    }

    /**
     * 保存要生成的文件信息
     *  @param classElement
     * @param fieldElement
     * @param fieldType
     */
    private void saveGenerateJavaFile(TypeElement classElement, Element fieldElement, String fieldType) {
        GenerateJavaFile generateJavaFile = mJavaFileMap.get(classElement.getQualifiedName().toString());
        if (generateJavaFile == null) {
            generateJavaFile = new GenerateJavaFile(mElementUtils,classElement, fieldElement,fieldType);
            mJavaFileMap.put(classElement.getQualifiedName().toString(), generateJavaFile);
        } else {
            generateJavaFile.addFieldElement(fieldElement.getSimpleName().toString(),fieldType);
        }
    }

}
