package lou.alex.fpml.builder;

import org.fpml.fpml_5.confirmation.*;

import javax.xml.datatype.XMLGregorianCalendar;

public class TradeBuilder {
    private static final String LEGAL_ENTITY_UNIT_SCHEME = "http://xxxxx/";
    private static final String BOOK_ACCOUNT_SCHEME = "http://xxxxx/";
    private static final String CP_ACCOUNT_SCHEME = "http://xxxxx/";

    private static final ObjectFactory OF = new ObjectFactory();
    private static final String SID_SCHEME = "http://mmmmmmm";
    private static final String CLTRDID_SCHEME = "http://xxxxxx";
    private static final String TRDID_SCHEME = "http://xxxxxx";

    private static class TradeFacts {
        private String trader;
        private String book;
        private String legalEntity;
        private String customerAccount;
        private XMLGregorianCalendar tradeDate;
        private String clientTradeId;
        private String tradeId;
    }

    public static class TradeParties {
        private final Party dealer;
        private final Party counterParty;
        private final Trade trade;

        private TradeParties(Party dealer, Party counterParty, Trade trade) {
            this.dealer = dealer;
            this.counterParty = counterParty;
            this.trade = trade;
        }

        public EventBuilder.EventTypeBuilder event() {
            return EventBuilder.builder(this);
        }
    }

    public static class TradeBookBuilder extends AbstractChainedBuilder<TradeFacts> {
        private TradeBookBuilder(TradeFacts config) {
            super(config);
        }

        public void setBook(String book) {

        }
    }

    private static TradeParties build(TradeFacts facts) {
        Party bookingParty = createParty("bookingParty");
        addLegalEntity(bookingParty, facts.legalEntity);
        Account bookAccount = createAccount(facts.book, "tradingBook", BOOK_ACCOUNT_SCHEME);
        Party counterParty = createParty("counterParty");
        Account cpAccount = createAccount(facts.customerAccount, "customerAccount", CP_ACCOUNT_SCHEME);

        PartyTradeInformation bookingPartyInfo = createPartyInfo("ExecutingBroker", bookingParty, bookAccount);
        bookingPartyInfo.getTrader().add(createTrader(facts.trader));
        PartyTradeInformation counterPartyInfo = createPartyInfo("CounterParty", counterParty, cpAccount);

        Trade trade = new Trade();
        TradeHeader header = new TradeHeader();
        header.getPartyTradeInformation().add(bookingPartyInfo);
        header.getPartyTradeInformation().add(counterPartyInfo);
        header.getPartyTradeIdentifier().add(createId(bookingParty, facts.clientTradeId, CLTRDID_SCHEME, "clientTradeId"));
        if (facts.tradeId != null) {
            header.getPartyTradeIdentifier().add(createId(bookingParty, facts.tradeId, TRDID_SCHEME, "tradeId"));
        }
        header.setTradeDate(createIdentifiedDate(facts.tradeDate, "tradeDate"));


        return new TradeParties(bookingParty, counterParty, trade);
    }

    private static PartyTradeIdentifier createId(Party party, String id, String scheme, String name) {
        PartyTradeIdentifier pti = new PartyTradeIdentifier();
        pti.setId(name);
        pti.setPartyReference(ref(party));
        TradeId tradeId = new TradeId();
        tradeId.setValue(id);
        tradeId.setTradeIdScheme(scheme);
        pti.setTradeId(tradeId);
        return pti;
    }

    private static Trader createTrader(String traderId) {
        Trader trader = new Trader();
        trader.setValue(traderId);
        trader.setTraderScheme(SID_SCHEME);
        return trader;
    }

    private static IdentifiedDate createIdentifiedDate(XMLGregorianCalendar date, String id) {
        IdentifiedDate identifiedDate = new IdentifiedDate();
        identifiedDate.setValue(date);
        identifiedDate.setId(id);
        return identifiedDate;
    }


    private static PartyTradeInformation createPartyInfo(String role, Party party, Account account) {
        PartyTradeInformation bookingPartyInfo = new PartyTradeInformation();
        bookingPartyInfo.setPartyReference(ref(party));
        RelatedParty relatedParty = new RelatedParty();
        relatedParty.setPartyReference(ref(party));
        relatedParty.setAccountReference(ref(account));
        PartyRole partyRole = new PartyRole();
        partyRole.setValue(role);
        relatedParty.setRole(partyRole);
        bookingPartyInfo.getRelatedParty().add(relatedParty);
        return bookingPartyInfo;
    }

    private static Account createAccount(String book, String id, String scheme) {
        Account account = new Account();
        account.setId(id);
        AccountId acctId = new AccountId();
        acctId.setValue(book);
        acctId.setAccountIdScheme(scheme);
        account.getContent().add(OF.createAccountAccountId(acctId));
        return account;
    }

    private static void addLegalEntity(Party bookingParty, String legalEntity) {
        BusinessUnit businessUnit = new BusinessUnit();
        businessUnit.setId("legalEntity");
        Unit unit = new Unit();
        businessUnit.setBusinessUnitId(unit);
        unit.setUnitScheme(LEGAL_ENTITY_UNIT_SCHEME);
        unit.setValue(legalEntity);
        bookingParty.getBusinessUnit().add(businessUnit);
    }

    private static Party createParty(String id) {
        Party party = new Party();
        party.setId(id);
        return party;
    }

    private static PartyReference ref(Party party) {
        PartyReference ref = new PartyReference();
        ref.setHref(party);
        return ref;
    }

    private static AccountReference ref(Account acct) {
        AccountReference ref = new AccountReference();
        ref.setHref(acct);
        return ref;
    }
}
