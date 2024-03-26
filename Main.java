import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SymbolLookup;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;
import java.lang.foreign.Linker;

public class Main {
    
    public static void main(String[] args) throws Throwable {       
        System.out.println("Ejecutando: "+sumarPorFuncionNativa(2, 2));

    }

    public static int sumarPorFuncionNativa(int a, int b) throws Throwable{
        try(Arena arena = Arena.ofConfined()){
            //Cargando la librería en C desde la carpeta actual.
            SymbolLookup funcionEnC = SymbolLookup.libraryLookup("./libfunciones.so", arena);
            
            //Obteniendo el puntero a la función que estamos necesitando.
            MemorySegment puntero = funcionEnC.find("sumar").get();
            System.out.println("El puntero a la función en C: "+puntero.address());

            //Creando la prototipo a lo interno de la función para realizar la llamada.
            //El primer valor es el retorno, los demás son los parametros de entrada.
            FunctionDescriptor prototipo = FunctionDescriptor.of(ValueLayout.JAVA_INT,
            ValueLayout.JAVA_INT, ValueLayout.JAVA_INT);

            //Obteniendo al función para ejecutar.
            MethodHandle funcion=Linker.nativeLinker().downcallHandle(puntero, prototipo);

            //Llamando al función, pasando los enteros como objetos Integer.
            return (Integer) funcion.invoke(Integer.valueOf(a), Integer.valueOf(b));

        }catch(Throwable ex){
            ex.printStackTrace();
            throw new RuntimeException("Error llamando la función");
        }
       
    }
}
