package lou.alex.fpml.builder;

import org.fpml.fpml_5.confirmation.AbstractEvent;

public class EventBuilder {

    static class EventFacts{
        private final TradeBuilder.TradeParties tradeParties;
        private AbstractEvent event;

        EventFacts(TradeBuilder.TradeParties tradeParties) {
            this.tradeParties = tradeParties;
        }
    }

    public static EventTypeBuilder builder(TradeBuilder.TradeParties tradeParties) {
        final EventFacts eventFacts = new EventFacts(tradeParties);
        return new EventTypeBuilder(eventFacts);
    }

    public static class EventTypeBuilder extends AbstractChainedBuilder<EventFacts> {

        private EventTypeBuilder(EventFacts config) {
            super(config);
        }

        

    }
}
