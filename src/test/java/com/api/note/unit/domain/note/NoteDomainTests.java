package com.api.note.unit.domain.note;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import com.api.note.domains.note.NoteDomain;
import com.api.note.domains.note.exceptions.InvalidNoteDomainException;

public class NoteDomainTests {
    @Test
    public void esperoQueRetorneUmNoteDomain() throws InvalidNoteDomainException {
        NoteDomain noteDomain = NoteDomain.validate("Foo Bar", "Foo bar");

        Assertions.assertThat(noteDomain.getTitle()).isEqualTo("Foo Bar");
        Assertions.assertThat(noteDomain.getContent()).isEqualTo("Foo bar");
    }
    
    @Test
    public void esperoQueRetorneUmErroDeTituloInvalidoDadoUmTituloVazio() {
        Assertions.assertThatExceptionOfType(InvalidNoteDomainException.class)
            .isThrownBy(() -> NoteDomain.validate("", "Foo bar"))
            .withMessage("title: must not be blank");
    }

    @Test
    public void esperoQueRetorneUmErroDeConteudoInvalidoDadoUmConteudoVazio() {
        Assertions.assertThatExceptionOfType(InvalidNoteDomainException.class)
            .isThrownBy(() -> NoteDomain.validate("Foo Bar", ""))
            .withMessage("content: must not be blank");
    }
}
