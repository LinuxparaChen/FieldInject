package com.chenzhanyang;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * Description:
 * Auther: chen_zhanyang.
 * Email: zhanyang.chen@gmail.com
 * Data: 2017/5/21.
 */

public class GenerateJavaFile {

    private Elements mElementUtils;
    private TypeElement ClassElement;
    private Map<String, String> FieldElements = new HashMap<>();

    public GenerateJavaFile() {

    }

    public GenerateJavaFile(Elements elementUtils, TypeElement classElement, Element fieldElement, String fieldType) {
        mElementUtils = elementUtils;
        this.ClassElement = classElement;
        this.FieldElements.put(fieldElement.getSimpleName().toString(), fieldType);
    }

    public void addFieldElement(String fieldName, String fieldType) {
        if (!FieldElements.containsKey(fieldName)) {
            FieldElements.put(fieldName, fieldType);
        }
    }

    public JavaFile buildJavaFile() {
        ParameterSpec param = ParameterSpec.builder(TypeName.get(ClassElement.asType()), "target", Modifier.FINAL).build();
        CodeBlock.Builder codeBuilder = CodeBlock.builder();
        for (Map.Entry<String, String> entry : FieldElements.entrySet()) {
            codeBuilder.addStatement("target.$N = new $N()",entry.getKey(), entry.getValue());
        }
        MethodSpec constructorMethod = MethodSpec.constructorBuilder().addParameter(param).addCode(codeBuilder.build()).addModifiers(Modifier.PUBLIC).build();

        TypeSpec classSpec = TypeSpec.classBuilder(ClassElement.getSimpleName().toString()+"_FieldInject").addMethod(constructorMethod).build();

        String packageName = mElementUtils.getPackageOf(ClassElement).getQualifiedName().toString();
        JavaFile javaFile = JavaFile.builder(packageName, classSpec).build();

        return javaFile;
    }

}
