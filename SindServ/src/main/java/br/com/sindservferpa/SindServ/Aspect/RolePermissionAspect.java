package br.com.sindservferpa.SindServ.Aspect;

import br.com.sindservferpa.SindServ.Annotation.RequireRole;
import br.com.sindservferpa.SindServ.Enum.Role;
import br.com.sindservferpa.SindServ.Util.JwtAuthenticationFilter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class RolePermissionAspect {
    @Around("@annotation(requireRole)")
    public Object checkPermission(ProceedingJoinPoint joinPoint, RequireRole requireRole) throws Throwable {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("Usuário não autenticado");
        }

        JwtAuthenticationFilter.JwtAuthenticationDetails details =
                (JwtAuthenticationFilter.JwtAuthenticationDetails) auth.getDetails();

        if (details == null) {
            throw new RuntimeException("Detalhes de autenticação não encontrados");
        }

        Role userRole = Role.valueOf(details.getRole());
        Role[] allowedRoles = requireRole.value();

        boolean hasPermission = Arrays.asList(allowedRoles).contains(userRole);

        if (!hasPermission) {
            throw new RuntimeException("Acesso negado. Permissão insuficiente.");
        }

        return joinPoint.proceed();
    }

}
