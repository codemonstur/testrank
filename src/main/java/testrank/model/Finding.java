package testrank.model;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;

public record Finding(String file, String method, int assertCount, int bodyLength) {
    public Finding(TypeDeclaration<?> type, MethodDeclaration method, int assertCount, int size) {
        this(type.getNameAsString(), method.getNameAsString(),  assertCount, size);
    }
}