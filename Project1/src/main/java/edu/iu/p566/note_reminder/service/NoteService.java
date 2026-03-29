package edu.iu.p566.note_reminder.service;

import edu.iu.p566.note_reminder.data.NoteRepository;
import edu.iu.p566.note_reminder.model.Note;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NoteService {

    private final NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public List<Note> getAllNotes() {
        return noteRepository.findAllByOrderByPinnedDescUpdatedAtDesc();
    }

    public List<Note> searchNotes(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return getAllNotes();
        }

        return noteRepository
                .findByTitleContainingIgnoreCaseOrContentContainingIgnoreCaseOrderByPinnedDescUpdatedAtDesc(
                        keyword, keyword
                );
    }

    public Note getNoteById(Long id) {
        return noteRepository.findById(id).orElse(null);
    }

    public void saveNote(Note note) {
        LocalDateTime now = LocalDateTime.now();

        if (note.getCreatedAt() == null) {
            note.setCreatedAt(now);
        }

        note.setUpdatedAt(now);
        noteRepository.save(note);
    }

    public void updateNote(Long id, Note updatedNote) {
        Note existing = getNoteById(id);
        if (existing == null) {
            return;
        }

        existing.setTitle(updatedNote.getTitle());
        existing.setContent(updatedNote.getContent());
        existing.setCategory(updatedNote.getCategory());
        existing.setPinned(updatedNote.isPinned());
        existing.setReminderTime(updatedNote.getReminderTime());
        existing.setUpdatedAt(LocalDateTime.now());

        noteRepository.save(existing);
    }

    public void deleteNote(Long id) {
        noteRepository.deleteById(id);
    }

  public List<Note> getDueReminders() {
    return noteRepository.findByReminderTimeBeforeOrderByReminderTimeAsc(LocalDateTime.now());
}
}