package com.daemin.clean_talk.moderation.repository;

import com.daemin.clean_talk.domain.ModerationResult;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModerationResultRepository extends JpaRepository<ModerationResult, Long> {

    Optional<ModerationResult> findByCommentId(Long commentId);
}
