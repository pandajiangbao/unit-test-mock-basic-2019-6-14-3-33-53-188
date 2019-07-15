package cashregister;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class CashRegisterTest {
    private final ByteArrayOutputStream outContent=new ByteArrayOutputStream();
    private final PrintStream originalOut=System.out;

    @BeforeEach
    public void setUpStreams(){
        System.setOut(new PrintStream(outContent));
    }
    @AfterEach
    private void restoreStreams(){
        System.setOut((originalOut));
    }
    @Test
    public void should_print_the_real_purchase_when_call_process() {
        //given
        Purchase purchase=new Purchase(new Item[]{
                new Item("A",1),
                new Item("B",2),
                new Item("C",3),
        });
        CashRegister cashRegister = new CashRegister(new Printer());
        //when
        cashRegister.process(purchase);
        //then
        assertThat(outContent.toString()).isEqualTo("A\t1.0\nB\t2.0\nC\t3.0\n");
    }

    @Test
    public void should_print_the_stub_purchase_when_call_process() {
        //given
        Purchase purchase=new Purchase(new Item[]{
                new Item("A",1),
                new Item("B",2),
                new Item("C",3),
        });
        CashRegister cashRegister = new CashRegister(new Printer(){
            @Override
            public void print(String printThis) {
                System.out.print("stub:"+printThis);
            }
        });
        //when
        cashRegister.process(purchase);
        //then
        Assertions.assertEquals("stub:A\t1.0\nB\t2.0\nC\t3.0\n",outContent.toString());
    }

    @Test
    public void should_verify_with_process_call_with_mockito() {
        //given
        Purchase purchase=new Purchase(new Item[]{
                new Item("A",1),
                new Item("B",2),
                new Item("C",3),
        });
        Printer printer = mock(Printer.class);
        CashRegister cashRegister = new CashRegister(printer);
        //when
        cashRegister.process(purchase);
        //then
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(printer).print(argumentCaptor.capture());
        Assertions.assertEquals("A\t1.0\nB\t2.0\nC\t3.0\n",argumentCaptor.getValue());
    }

}
