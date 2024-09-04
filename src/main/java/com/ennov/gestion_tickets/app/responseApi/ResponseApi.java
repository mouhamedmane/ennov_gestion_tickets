package com.ennov.gestion_tickets.app.responseApi;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ResponseApi<T> {
    private String status;
    private T data;
    private List<String>  errorMessage;

    public static <T> ResponseApi<T> success(T data){
        return new ResponseApi<>("ok", data, null);
    }

    public static <T> ResponseApi<T> fail(List<String>  errorMessage){
        return new ResponseApi<>("echec", null, errorMessage);
    }

}
