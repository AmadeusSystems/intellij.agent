package intellij.agent.build;

import java.util.List;

import amadeus.maho.lang.SneakyThrows;
import amadeus.maho.util.build.IDEA;
import amadeus.maho.util.build.Jar;
import amadeus.maho.util.build.Javac;
import amadeus.maho.util.build.Module;
import amadeus.maho.util.build.Workspace;
import amadeus.maho.util.depend.Project;
import amadeus.maho.util.depend.Repository;

@SneakyThrows
public class Build {
    
    public static final Workspace workspace = Workspace.here();
    
    public static final Repository maven = Repository.maven();
    
    public static final Module module = new Module("intellij.agent", maven.resolveModuleDependencies(new Project.Dependency.Holder().all("org.ow2.asm:asm:+").dependencies()));
    
    static { sync(); }
    
    public static void sync() = IDEA.generateAll(workspace, "11", false, List.of(Module.build(), module));
    
    public static void build() {
        workspace.clean().flushMetadata();
        Javac.compile(workspace, module);
        final Jar.ClassPath classPath = { module.dependencies() };
        Jar.pack(workspace, module, Jar.manifest(null, new Jar.Agent("intellij.agent.Agent"), classPath));
        classPath.copyTo(workspace, module);
    }

}
