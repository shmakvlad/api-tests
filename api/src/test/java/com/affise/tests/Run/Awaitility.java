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


// 6

    /*public static ConversionResponse generateConversionByClick(ClickResponse click) {
        return validateConversion(() -> generateConversion(click));
    }

    public static ConversionResponse generateConversionWithStatus(ClickResponse click, int status) {
        return validateConversion(() -> generateConversion(click, status));
    }

    public static ConversionResponse generateConversionWithGoal(ClickResponse click, String goal) {
        return validateConversion(() -> generateConversion(click, goal));
    }

    public static ConversionResponse validateConversion(Supplier<ConversionResponse> supplier) {
        final ConversionResponse[] conversion = new ConversionResponse[]{new ConversionResponse()};
        conversionConfigurationTimeout().until(() -> {
                    conversion[0] = supplier.get();
                    return getConversionStatus(conversion[0]);
                }, IsEqual.equalTo(1)
        );
        return conversion[0];
    }

    public static ConditionFactory conversionConfigurationTimeout() {
        return await().ignoreExceptions()
                .atMost(Duration.ofMillis(getConfig().conversionTimeout()))
                .pollDelay(Duration.ofMillis(getConfig().conversionRequestDelayTimeout()))
                .pollInterval(Duration.ofMillis(getConfig().conversionRequestIntervalTimeout()));
    }

    public static ConditionFactory waitForConversionCreateConfig() {
        return await().ignoreExceptions()
                .atMost(Duration.ofSeconds(20))
                .pollDelay(Duration.ofSeconds(15))
                .pollInterval(Duration.ofSeconds(4));
    }

    @ParameterizedTest
    @CsvSource({
            "1, 'confirmed', 31",
            "2, 'pending',   41",
            "3, 'declined',  51",
            "5, 'hold',      61"
    })
    @DisplayName("Conversions come with expected status")
    void getConversionsStatus1(int intStatus, String stringStatus, String limit) {

        ClickResponse click = makeClick();
        generateConversionWithStatus(click, intStatus);
        String conversionId = getConversionIdByClickId(click.getClickid());

        waitForConversionCreateConfig().alias("Wait for the conversion to be created").until(
                () -> statisticsConversionApiService.checkStatisticsConversions(ROOT_ADMIN_KEY, dateFrom, dateTo, limit)
                        .getResponse().path(String.format("conversions.find{it.id == '%s'}.id", conversionId)),
                equalTo(conversionId));

        ConversionsList conversionsList =
                statisticsConversionApiService.checkStatisticsConversions(ROOT_ADMIN_KEY, dateFrom, dateTo, limit)
                        .asClass(ConversionsList.class);

        assertThat(conversionsList.getConversions().stream()
                .filter(x -> x.getId().equals(conversionId))
                .map(Conversion::getStatus).findFirst().get())
                .isEqualTo(stringStatus);
    }

    @ParameterizedTest
    @CsvSource({
            "1, 'confirmed', 35",
            "2, 'pending',   36",
            "3, 'declined',  37",
            "5, 'hold',      38"
    })
    @DisplayName("Conversions come with expected status")
    void getConversionsStatus2(int intStatus, String stringStatus, String limit) {

        ClickResponse click = makeClick();
        generateConversionWithStatus(click, intStatus);

        waitForConversionCreateConfig().alias("Wait for the conversion to be created").until(() ->
                        statisticsConversionApiService.checkStatisticsConversions(ROOT_ADMIN_KEY, dateFrom, dateTo, limit)
                                .getResponse().path(String.format("conversions.find{it.action_id == '%s'}.status", click.getClickid())),
                equalTo(stringStatus));
    }*/

}
