package lou.alex.fpml.builder;

public abstract class AbstractChainedBuilder <T>{
    protected final T config;

    protected AbstractChainedBuilder(T config) {
        this.config = config;
    }
}
