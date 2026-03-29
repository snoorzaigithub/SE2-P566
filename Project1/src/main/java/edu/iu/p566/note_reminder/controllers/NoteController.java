package edu.iu.p566.note_reminder.controllers;

import edu.iu.p566.note_reminder.model.Note;
import edu.iu.p566.note_reminder.service.NoteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/notes")
    public String notes(@RequestParam(required = false) String keyword, Model model) {
        model.addAttribute("notes", noteService.searchNotes(keyword));
        model.addAttribute("keyword", keyword);
        return "notes";
    }

@GetMapping("/api/reminders/due")
@ResponseBody
public List<Map<String, Object>> getDueReminders() {
    List<Note> dueNotes = noteService.getDueReminders();

    return dueNotes.stream().map(note -> {
        Map<String, Object> item = new HashMap<>();
        item.put("id", note.getId());
        item.put("title", note.getTitle());
        item.put("content", note.getContent());
        item.put("reminderTime", note.getReminderTime() != null ? note.getReminderTime().toString() : null);
        return item;
    }).toList();
}

    @GetMapping("/notes/new")
    public String newNoteForm(Model model) {
        model.addAttribute("note", new Note());
        return "note-form";
    }

    @PostMapping("/notes")
    public String saveNote(@ModelAttribute Note note) {
        noteService.saveNote(note);
        return "redirect:/notes";
    }

    @GetMapping("/notes/edit/{id}")
    public String editNote(@PathVariable Long id, Model model) {
        Note note = noteService.getNoteById(id);
        if (note == null) {
            return "redirect:/notes";
        }
        model.addAttribute("note", note);
        return "note-form";
    }

    @PostMapping("/notes/update/{id}")
    public String updateNote(@PathVariable Long id, @ModelAttribute Note updatedNote) {
        noteService.updateNote(id, updatedNote);
        return "redirect:/notes";
    }

    @PostMapping("/notes/delete/{id}")
    public String deleteNote(@PathVariable Long id) {
        noteService.deleteNote(id);
        return "redirect:/notes";
    }
}