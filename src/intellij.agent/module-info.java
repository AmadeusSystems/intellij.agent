module intellij.agent {
    
    requires java.instrument;
    requires org.objectweb.asm;
    
    opens intellij.agent;
    
}
