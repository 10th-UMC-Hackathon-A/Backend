package com.example.umc.domain.penalty.entity;

import com.example.umc.domain.room.entity.Room;
import com.example.umc.global.common.base.BaseEntity;
import com.example.umc.global.common.converter.StringListConverter;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(
        name = "penalty_draw_result",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_penalty_draw_room_round",
                        columnNames = {"room_id", "draw_round"}
                )
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class PenaltyDrawResult extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "penalty_draw_result_id")
    private Long penaltyDrawResultId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Column(name = "draw_round", nullable = false)
    private Integer drawRound;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "penalty_id", nullable = false)
    private Penalty penalty;

    @Column(name = "prize_index", nullable = false)
    private Integer prizeIndex;

    @Column(name = "draw_penalty_list")
    @Convert(converter = StringListConverter.class)
    private List<String> drwaPenaltyList;
}
