package br.edu.ufape.personal_trainer.dto;

public record LoginRequest(
    String email,
    String senha
) {}
