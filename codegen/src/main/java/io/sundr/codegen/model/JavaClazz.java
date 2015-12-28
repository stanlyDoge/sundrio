/*
 * Copyright 2015 The original authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package io.sundr.codegen.model;

import io.sundr.codegen.Clazz;
import io.sundr.codegen.functions.JavaClazzToReferences;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class JavaClazz extends AttributeSupport implements Clazz<JavaType, JavaProperty> {

    private final JavaType type;
    private final Set<JavaType> annotations;
    private final Set<JavaMethod> methods;
    private final Set<JavaMethod> constructors;
    private final Set<JavaProperty> fields;
    private final Set<JavaType> imports;
    private final Set<JavaClazz> nested;

    public JavaClazz(JavaType type, Set<JavaType> annotations, Set<JavaMethod> constructors, Set<JavaMethod> methods, Set<JavaProperty> fields, Set<JavaType> imports, Map<String, Object> attributes, Set<JavaClazz> nested) {
        super(attributes);
        this.type = type;
        this.annotations = annotations;
        this.methods = methods;
        this.constructors = constructors;
        this.fields = fields;
        this.imports = imports;
        this.nested = nested;
    }

    private Set<JavaType> getReferencedTypes() {
        return JavaClazzToReferences.FUNCTION.apply(this);
    }

    public Set<JavaType> getAnnotations() {
        return annotations;
    }

    public Set<JavaMethod> getConstructors() {
        return constructors;
    }

    @Override
    public JavaType getType() {
        return type;
    }

    @Override
    public Set<JavaMethod> getMethods() {
        return methods;
    }

    @Override
    public Set<JavaProperty> getFields() {
        return fields;
    }

    @Override
    public Set<JavaType> getImports() {
        Set<JavaType> result = new CopyOnWriteArraySet<JavaType>();
        Set<JavaType> tmp = new CopyOnWriteArraySet<JavaType>();
        tmp.addAll(this.imports);
        tmp.addAll(getReferencedTypes());

        for (JavaType t : tmp) {
            if (t.getClassName().equals(getType().getClassName())) {
                continue;
            } else if (t.getPackageName() == null) {
                continue;
            } else if (t.getPackageName().equals("java.lang")) {
                continue;
            } else if (!t.getPackageName().equals(getType().getPackageName())) {
                result.add(t);
            }
        }
        return result;
    }

    public Set<JavaClazz> getNested() {
        return nested;
    }

    @Override
    public String toString() {
        return type.getClassName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JavaClazz javaClazz = (JavaClazz) o;

        return !(type != null ? !type.equals(javaClazz.type) : javaClazz.type != null);

    }

    @Override
    public int hashCode() {
        return type != null ? type.hashCode() : 0;
    }
}
