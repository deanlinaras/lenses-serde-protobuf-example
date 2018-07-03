package com.landoop.examples.lenses.serde.protobuf;

import com.landoop.examples.lenses.serde.protobuf.generated.CardData;

import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class CreditCardGenerator implements Supplier<CardData.CreditCard> {

    private final Random random = new Random();

    private final String[] countries = new String[]{"United States", "United Kingdom", "South Africa", "Canada", "Australia"};
    private final String[] currencies = new String[]{"GBP", "EUR", "USD", "AUD", "RAN", "CAD"};

    @Override
    public CardData.CreditCard get() {
        CardData.CreditCard.CardType cardType = CardData.CreditCard.CardType.values()[random.nextInt(CardData.CreditCard.CardType.values().length)];
        String number = random.ints(1, 9).limit(16).mapToObj(i -> "" + i).collect(Collectors.joining());
        return CardData.CreditCard.newBuilder()
                .setBlocked(random.nextBoolean())
                .setCardType(cardType)
                .setName("A.N.Other")
                .setCountry(countries[random.nextInt(countries.length)])
                .setCurrency(currencies[random.nextInt(currencies.length)])
                .setCardNumber(number)
                .build();
    }


}
