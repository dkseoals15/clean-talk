package com.daemin.clean_talk.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * 모든 엔티티에서 공통으로 사용하는 생성일시와 수정일시를 관리하는 추상 클래스입니다.
 *
 * <p>{@link MappedSuperclass}를 통해 하위 엔티티의 테이블 컬럼으로 매핑되며,
 * {@link AuditingEntityListener}가 저장 및 수정 시각을 자동으로 채웁니다.
 */
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    /** 엔티티가 처음 저장된 일시입니다. */
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /** 엔티티가 마지막으로 수정된 일시입니다. */
    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
