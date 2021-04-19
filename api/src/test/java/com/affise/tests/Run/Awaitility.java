package com.affise.tests.Run;

public class Awaitility {

// 1
    /*
    public static void makeClickAndConversion(ClickResponse makeClick) {
        ClickResponse clickResponse = makeClick(makeClick, 5);

        await("Wait when conversion on click should have status 1")
            .given()
                .ignoreExceptions()
                .and()
                .ignoreException(AssertionError.class)
            .with()
                .atMost(Duration.ofSeconds(5))
                .pollDelay(Duration.ofMillis(200))
                .pollInterval(Duration.ofMillis(200))
            .then()
                .until(() -> getConversionStatus(generateConversion(clickResponse)), equalTo(1));

        System.out.println("Count of created conversions: " + ++conversionSuccessCounter);
    }
    */

// 2
    /*public static ConversionResponse generateConversionByClick(ClickResponse click) {
            final ConversionResponse[] conversion = new ConversionResponse[]{ new ConversionResponse() };

            await().ignoreExceptions()
                    .atMost(Duration.ofMillis(getConfig().conversionTimeout()))
                    .pollDelay(Duration.ofMillis(getConfig().conversionRequestDelayTimeout()))
                    .pollInterval(Duration.ofMillis(getConfig().conversionRequestIntervalTimeout()))
                    .until(
                            () -> {
                                conversion[0] = generateConversion(click);
                                return getConversionStatus(conversion[0]);
                            }, Matchers.equalTo(1)
                    );
            return conversion[0];
        return validateConversion(() -> generateConversion(click));
    }*/

// 3
    /*public static ConversionResponse generateConversionWithStatus(ClickResponse click, int status) {
        return validateConversion(() -> generateConversion(click, status));
    }

    public static ConversionResponse validateConversion(Supplier<ConversionResponse> supplier) {
        final ConversionResponse[] conversion = new ConversionResponse[]{ new ConversionResponse() };
        await().ignoreExceptions()
                .atMost(Duration.ofMillis(getConfig().conversionTimeout()))
                .pollDelay(Duration.ofMillis(getConfig().conversionRequestDelayTimeout()))
                .pollInterval(Duration.ofMillis(getConfig().conversionRequestIntervalTimeout()))
                .until(
                        () -> {
                            conversion[0] = supplier.get();
                            return getConversionStatus(conversion[0]);
                        }, equalTo(1)
                );
        return conversion[0];
    }*/

// 4
    /*public static void makeClickAndConversion() {
        ClickResponse clickResponse = makeClick();
        final int[] counterForInvalidConversions = {0};
        await("Wait when conversion on click should have status 1")
                .ignoreExceptions()
                .atMost(Duration.ofMillis(getConfig().conversionTimeout()))
                .pollDelay(Duration.ofMillis(getConfig().conversionRequestDelayTimeout()))
                .pollInterval(Duration.ofMillis(getConfig().conversionRequestIntervalTimeout()))
                .until(
                        () -> {
                            System.out.println("Make conversion attempt number: " + ++counterForInvalidConversions[0]);
                            return getConversionStatus(generateConversion(clickResponse));
                        }, equalTo(1)
                );
        System.out.println("Count of created conversions: " + ++conversionSuccessCounter);
    }*/

// 5
   /* public static ClickResponse makeClick() {
        ClickResponse clickResponse = click();
        long start = System.currentTimeMillis();
        long end = getConfig().clickTimeout() * 2 + start;
        int counterForInvalidClicks = 0;
        while (System.currentTimeMillis() < end) {
            if (clickResponse.getClickid().matches("^[0-9a-fA-F]{24}$")) {
                System.out.println("Count of created clicks: " + ++clickSuccessCounter);
                return clickResponse;
            }
            System.out.println("Exception: Make new click. Retry attempt number: " + ++counterForInvalidClicks);
            clickResponse = click();
        }
        throw new RuntimeException("Failed to get a valid click");
    }*/
}
