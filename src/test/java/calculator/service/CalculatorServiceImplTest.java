package calculator.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

class CalculatorServiceImplTest {

    private static CalculatorServiceImpl service;

    @BeforeAll
    static void setup() {
        service = new CalculatorServiceImpl();
    }

    @ParameterizedTest
    @MethodSource("examplesToCalculate")
    void calculateTest(String example, String expected) {
        Assertions.assertEquals(expected, service.calculate(example));
    }

    private static Stream<Arguments> examplesToCalculate() {
        return Stream.of(
                arguments("2 + 2", "4"),
                arguments("2 - 2", "0"),
                arguments("2 * 2", "4"),
                arguments("2 / 2", "1"),
                arguments("1 + 2.5", "3.5"),
                arguments("2 + 3 * 6 / ( ( 7 - 2 ) * 2 )", "3.8"),
                arguments("( 5 + 3 ) * ( 2 * ( 2 + 2 ) )", "64"),
                arguments("2 + 7 * 3 + 3 / 3", "24"),
                arguments("85 * 2 + ( 36 - 8 / 2 - 49 * 8 + 68 / 2 ) + 89 - ( 35 * 6 - 7 )", "-270"),
                arguments("2 / 0", "ERROR"),
                arguments("2 * ( 3 + 4 ) )", "ERROR")
        );
    }
}
