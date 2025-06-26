package br.com.sindservferpa.SindServ.Annotation;

import br.com.sindservferpa.SindServ.Enum.Role;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireRole {
    Role[] value();
}

//Cria uma anotação pra definir qual role podera acessar uma rota