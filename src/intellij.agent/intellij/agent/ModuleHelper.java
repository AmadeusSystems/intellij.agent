package intellij.agent;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;

import amadeus.maho.lang.SneakyThrows;

@SneakyThrows
public class ModuleHelper {
    
    public static final Field IMPL_LOOKUP = MethodHandles.Lookup.class.getDeclaredField("IMPL_LOOKUP");
    static { IMPL_LOOKUP.setAccessible(true); }
    
    public static final MethodHandles.Lookup lookup = (MethodHandles.Lookup) IMPL_LOOKUP.get(null);
    
    // JVM_AddModuleExportsToAll
    private static final MethodHandle implAddOpens = lookup.findVirtual(Module.class, "implAddOpens", MethodType.methodType(void.class, String.class));
    
    public static void addOpens(final Module module) = module.getPackages().forEach(packageName -> implAddOpens.invoke(module, packageName));
    
    public static void openAllBootModule() = ModuleLayer.boot().modules().forEach(ModuleHelper::addOpens);
    
}
