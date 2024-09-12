package com.quantum.holdup.Page;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class PagingButtonInfo {

    private int currentPage;
    private int startPage;
    private int endPage;
}
