package com.company.client.CommandsProcessor;

import com.company.client.Communication.ClientConsoleInputProcessor;
import com.company.client.Communication.ClientPrinter;
import com.company.shared.annotations.FieldAnnotation;
import com.company.shared.objects.Chapter;
import com.company.shared.objects.Coordinates;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class ClientCommandsHelper {
    public static <T extends Class<?>> Object getNewObject(T myClass, ClientConsoleInputProcessor inp, ClientPrinter queryClientPrinter) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        ArrayList<Object> arr = new ArrayList<>();
        Field[] fields = myClass.getDeclaredFields();

        for(Field field: fields) {
            field.setAccessible(true);
            FieldAnnotation fa = field.getDeclaredAnnotation(FieldAnnotation.class);
            if (fa == null) continue;
            if (fa.ignore()) {
                queryClientPrinter.print("print", field.getName() + " will be generated automatically");
                continue;
            }
            queryClientPrinter.print("rawPrint", "Enter " + field.getName() + ": ");
            Class container = field.getType();
            if (container.isEnum()) queryClientPrinter.print("print", container.getEnumConstants());
            switch (fa.type()) {
                case "Integer":
                    arr.add(inp.getInt(fa.min_value(), fa.max_value()));
                    break;
                case "Long":
                    arr.add((long)inp.getInt(fa.min_value(), fa.max_value()));
                    break;
                case "String":
                    arr.add(inp.getString(0));
                    break;
                case "Enum":
                    arr.add(inp.getEnum(container));
                    break;
                case "Coordinates":
                    queryClientPrinter.print("print", "");
                    arr.add(getNewObject(Coordinates.class, inp, queryClientPrinter));
                    break;
                case "Chapter":
                    queryClientPrinter.print("print", "");
                    arr.add(getNewObject(Chapter.class, inp, queryClientPrinter));
                    break;
            }
        }
        return myClass.getDeclaredConstructors()[0].newInstance(arr.toArray());
    }
}
