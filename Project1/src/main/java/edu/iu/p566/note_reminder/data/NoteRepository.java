package edu.iu.p566.note_reminder.data;

import edu.iu.p566.note_reminder.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {

    List<Note> findAllByOrderByPinnedDescUpdatedAtDesc();

    List<Note> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCaseOrderByPinnedDescUpdatedAtDesc(
            String titleKeyword, String contentKeyword
    );

    List<Note> findByReminderTimeBeforeOrderByReminderTimeAsc(LocalDateTime time);
}