package com.lotu_us.usedbook.domain.entity;

import com.lotu_us.usedbook.domain.dto.ItemDTO;
import com.lotu_us.usedbook.domain.enums.Category;
import com.lotu_us.usedbook.domain.enums.SaleStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member seller;

    private String title;

    @Enumerated(EnumType.STRING)
    private Category category;

    private int price;

    private int stock;

    private String content;

    @Enumerated(EnumType.STRING)
    private SaleStatus saleStatus = SaleStatus.READY;

    private LocalDateTime createTime;

    private int likeCount;

    private int viewCount;

    private int commentCount;



    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<ItemFile> files = new ArrayList<>();
    //files는 단순히 파일을 가져오기만 할 것이다. item의 외래키는 file이 관리할 것이다.

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();
    //comments는 단순히 댓글을 가져오기만 할 것이다. 외래키는 comment가 관리할 것이다.


    @Builder
    public Item(Member seller, String title, Category category, int price, int stock, String content) {
        this.seller = seller;
        this.title = title;
        this.category = category;
        this.price = price;
        this.stock = stock;
        this.content = content;
    }

    @PrePersist //최초 Persist될 때 수행
    public void setCreateTime() {
        this.createTime = LocalDateTime.now();
    }

//    @PreUpdate  //Persist 이후 값이 변경될때마다 수행
//    public void setUpdateTime() {
//        this.createTime = LocalDateTime.now();
//    }


    public void changeContents(ItemDTO.Write itemDTO) {
        this.title = itemDTO.getTitle();
        this.category = itemDTO.getCategory();
        this.price = itemDTO.getPrice();
        this.stock = itemDTO.getStock();
        this.content = itemDTO.getContent();
    }


    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", seller=" + seller +
                ", title='" + title + '\'' +
                ", category=" + category +
                ", price=" + price +
                ", stock=" + stock +
                ", content='" + content + '\'' +
                ", saleStatus=" + saleStatus +
                ", createTime=" + createTime +
                ", likeCount=" + likeCount +
                ", viewCount=" + viewCount +
                ", files=" + files +
                ", comments=" + comments +
                '}';
    }
}
