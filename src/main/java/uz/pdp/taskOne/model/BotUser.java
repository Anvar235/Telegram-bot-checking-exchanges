package uz.pdp.taskOne.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.PackagePrivate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@PackagePrivate

public class BotUser {

    Integer step;
    Currency currency;
    boolean conversionType;

}
