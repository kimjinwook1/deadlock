package com.example.deadlock.original.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DooJo {
    private Jo jo;
    private Doo doo;

    public DooJo(final Jo jo, final Doo doo) {
        this.jo = jo;
        this.doo = doo;
    }
}
