package com.quantum.holdup.Page;

import org.springframework.data.domain.Page;

public class Pagination {

    public static PagingButtonInfo getPagingButtonInfo(Page page) {

        //현재 페이지에 대한 정보 +1 (숫자 체계가 0부터 시작하기 때문에 사용자가 보는 시점에 맞게
        int currentPage = page.getNumber() + 1;

        // 페이지 버튼 기본 갯수
        int defaultButtonCount = 10;

        // 현재 시작 페이지를 계산'
        int startPage =
                (int) (Math.ceil((double) currentPage / defaultButtonCount) - 1)
                        * defaultButtonCount + 1;

        // 끝 페이지 계산
        int endPage = startPage + defaultButtonCount - 1;

        // 실제 총페이지가 endPage보다 작으면 endPage를 총 페이지로
        if (page.getTotalPages() < endPage) {
            endPage = page.getTotalPages();
        }

        // 토탈 페이지가 0이거나 끝페이지가 0이면 끝페이지를 시작페이지로 설정
        if (page.getTotalPages() == 0 && endPage == 0) {
            endPage = startPage;
        }

        return new PagingButtonInfo(currentPage, startPage, endPage);
    }
}
