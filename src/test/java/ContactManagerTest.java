
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.w3c.dom.ls.LSOutput;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ContactManagerTest {

    ContactManager contactManager;
    @BeforeAll
    public static void setUpAll(){
        System.out.println("-----> Should Print Before All Tests");
    }

    @BeforeEach
    public void setUp(){
        contactManager = new ContactManager();
    }

    @Test
    @DisplayName("Should Create Contact")
    @Disabled
    public void shouldCreateContact(){
        contactManager.addContact("Diego","Castro","3131234567");
        assertFalse(contactManager.getAllContacts().isEmpty());
        assertEquals(1,contactManager.getAllContacts().size());
        Assertions.assertTrue(contactManager.getAllContacts().stream()
                .filter(contact -> contact.getFirstName().equals("Diego") && contact.getLastName().equals("Castro") &&
                        contact.getPhoneNumber().equals("3131234567"))
                .findAny()
                .isPresent());
    }

    @Test
    @DisplayName("Should Not Create Contact When First Name is Null")
    public void shouldThrowRuntimeExceptionFirstNameIsNull(){
        Assertions.assertThrows(RuntimeException.class, () -> {
            contactManager.addContact(null, "Castro", "3131234567");
        });
    }

    @Test
    @DisplayName("Should Not Create Contact When Last Name is Null")
    public void shouldThrowRuntimeExceptionLastNameIsNull(){
        Assertions.assertThrows(RuntimeException.class, () -> {
            contactManager.addContact("Diego", null, "3131234567");
        });
    }

    @Test
    @DisplayName("Should Not Create Contact When Phone Number is Null")
    public void shouldThrowRuntimeExceptionPhoneNumberIsNull(){
        Assertions.assertThrows(RuntimeException.class, () -> {
            contactManager.addContact("Diego", "Castro", null);
        });
    }

    @AfterEach
    public void tearDown(){
        System.out.println("Should Execute After Each Test");
    }

    @AfterAll
    public  void tearDownAll(){
        System.out.println("Should be executed at the end fo the Test");
    }

    @Test
    @DisplayName("Should Create Contact ONly on Mac Os")
    @EnabledOnOs(value = OS.MAC, disabledReason = "Enabled only on MAC OS")
    public void shouldCreateContactOnlyMAC(){
        contactManager.addContact("Diego","Castro","3101234567");
        assertFalse(contactManager.getAllContacts().isEmpty());
        assertEquals(1, contactManager.getAllContacts().size());
        Assertions.assertTrue(contactManager.getAllContacts().stream()
                .filter(contact -> contact.getFirstName().equals("Diego") &&
                        contact.getLastName().equals("Castro") &&
                        contact.getPhoneNumber().equals("3101234567"))
                .findAny()
                .isPresent());
    }

    @Test
    @DisplayName("Should Create Contact ONly on Windows Os")
    @EnabledOnOs(value = OS.WINDOWS, disabledReason = "Enabled only on Windows OS")
    public void shouldCreateContactOnlyOnWindows(){
        contactManager.addContact("Diego","Castro","3101234567");
        assertFalse(contactManager.getAllContacts().isEmpty());
        assertEquals(1, contactManager.getAllContacts().size());
        Assertions.assertTrue(contactManager.getAllContacts().stream()
                .filter(contact -> contact.getFirstName().equals("Diego") &&
                        contact.getLastName().equals("Castro") &&
                        contact.getPhoneNumber().equals("3101234567"))
                .findAny()
                .isPresent());
    }

    @Test
    @DisplayName("Test Contact Creation on Developer Machine")
    public void shouldTestContactCreationOnDEV(){
        Assumptions.assumeTrue("DEV".equals(System.getProperty("ENV")));
        contactManager.addContact("Diego","Castro","3131234567");
        assertFalse(contactManager.getAllContacts().isEmpty());
        assertEquals(1,contactManager.getAllContacts().size());
    }

    @DisplayName("Repeated Contact Creation Test 5 Times")
    @RepeatedTest(value = 5,
            name = "Repeating Contact Creation Test {currentRepetition} of {totalRepetition}")
    public void shouldTestContactCreationRepeatedly(){
        contactManager.addContact("Diego","Castro", "3131234567");
        assertFalse(contactManager.getAllContacts().isEmpty());
        assertEquals(1, contactManager.getAllContacts().size());
    }

    @DisplayName("Repeat Contact Creation Test 5 Times")
    @ParameterizedTest
    @ValueSource(strings = {"3131234567", "3131234567", "3131234567"})
    public void shouldTestContactCreationUsingValueSource(String phoneNumber){
        contactManager.addContact("Diego", "Castro", phoneNumber);
        assertFalse(contactManager.getAllContacts().isEmpty());
        assertEquals(1,contactManager.getAllContacts().size());
    }

    @DisplayName("Method Source Case - Phone Number should match the requireds format")
    @ParameterizedTest
    @MethodSource("phoneNumberList")
    public void shouldTestPhoneNumberFormatUsingMethodSource(String phoneNumber){
        contactManager.addContact("Diego", "Castro", phoneNumber);
        assertFalse(contactManager.getAllContacts().isEmpty());
        assertEquals(1, contactManager.getAllContacts().size());
    }

    private static List<String> phoneNumberList(){
        return Arrays.asList("3131234567", "3132345678", "3133456789");
    }

    @DisplayName("CSV Source Case - Phone Number should match the required Format")
    @ParameterizedTest
    @CsvSource({"3131234567", "3132345678",  "3133456789"})
    public void shouldTestPhoneNumberFormatUsingCSVSource(String phoneNumber){
        contactManager.addContact("Diego", "Castro", phoneNumber);
        assertFalse(contactManager.getAllContacts().isEmpty());
        assertEquals(1, contactManager.getAllContacts().size());
    }

    @DisplayName("CSV Source Case - Phone Number should match the required Format")
    @ParameterizedTest
    @CsvFileSource(resources = "/data.csv")
    public void shouldTestPhoneNumberFormatUsingCSVFileSource(String phoneNumber){
        contactManager.addContact("Diego", "Castro", phoneNumber);
        assertFalse(contactManager.getAllContacts().isEmpty());
        assertEquals(1, contactManager.getAllContacts().size());
    }

    @Nested
    class RepeatedNestedTest{
        @DisplayName("Repeat Contact Creation Test 5 Times")
        @ParameterizedTest
        @ValueSource(strings = {"3131234567", "3131234567", "3131234567"})
        public void shouldTestContactCreationUsingValueSource(String phoneNumber){
            contactManager.addContact("Diego", "Castro", phoneNumber);
            assertFalse(contactManager.getAllContacts().isEmpty());
            assertEquals(1,contactManager.getAllContacts().size());
        }

    }

    @Nested
    class parameterizedNestedTest{
        @DisplayName("Repeated Contact Creation Test 5 Times")
        @RepeatedTest(value = 5,
                name = "Repeating Contact Creation Test {currentRepetition} of {totalRepetition}")
        public void shouldTestContactCreationRepeatedly(){
            contactManager.addContact("Diego","Castro", "3131234567");
            assertFalse(contactManager.getAllContacts().isEmpty());
            assertEquals(1, contactManager.getAllContacts().size());
        }

        @DisplayName("Repeat Contact Creation Test 5 Times")
        @ParameterizedTest
        @ValueSource(strings = {"3131234567", "3131234567", "3131234567"})
        public void shouldTestContactCreationUsingValueSource(String phoneNumber){
            contactManager.addContact("Diego", "Castro", phoneNumber);
            assertFalse(contactManager.getAllContacts().isEmpty());
            assertEquals(1,contactManager.getAllContacts().size());
        }

        @DisplayName("CSV Source Case - Phone Number should match the required Format")
        @ParameterizedTest
        @CsvFileSource(resources = "/data.csv")
        public void shouldTestPhoneNumberFormatUsingCSVFileSource(String phoneNumber){
            contactManager.addContact("Diego", "Castro", phoneNumber);
            assertFalse(contactManager.getAllContacts().isEmpty());
            assertEquals(1, contactManager.getAllContacts().size());
        }

    }
}
