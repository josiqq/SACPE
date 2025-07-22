package com.sacpe.dto;

// DTO para el calendario (FullCalendar)
public class CitaDTO {
    private Long id;
    private String title;
    private String start;
    private String end;
    private String color; // Opcional: para colorear las citas por estado

    public CitaDTO(Long id, String title, String start, String end, String color) {
        this.id = id;
        this.title = title;
        this.start = start;
        this.end = end;
        this.color = color;
    }
    
    // --- Getters y Setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getStart() { return start; }
    public void setStart(String start) { this.start = start; }
    public String getEnd() { return end; }
    public void setEnd(String end) { this.end = end; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
}