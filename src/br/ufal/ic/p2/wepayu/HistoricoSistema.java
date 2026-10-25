package br.ufal.ic.p2.wepayu;

import br.ufal.ic.p2.wepayu.exception.NadaParaDesfazerException;
import br.ufal.ic.p2.wepayu.exception.NadaParaRefazerException;
import br.ufal.ic.p2.wepayu.models.EstadoSistema;

import java.util.ArrayDeque;
import java.util.Deque;

public class HistoricoSistema {
    private final Deque<EstadoSistema> undo = new ArrayDeque<>();
    private final Deque<EstadoSistema> redo = new ArrayDeque<>();

    public void salvarParaUndo(EstadoSistema estadoAtual) {
        undo.push(estadoAtual.copiar());
    }

    public void confirmarOperacao() {
        redo.clear();
    }

    public EstadoSistema descartarUltimoUndo() {
        return undo.pop();
    }

    public EstadoSistema undo(EstadoSistema estadoAtual) throws NadaParaDesfazerException {
        if (undo.isEmpty()) {
            throw new NadaParaDesfazerException();
        }
        redo.push(estadoAtual.copiar());
        return undo.pop();
    }

    void cancelarUndo(EstadoSistema estadoDesfeito) {
        undo.push(estadoDesfeito.copiar());
        redo.pop();
    }

    public EstadoSistema redo(EstadoSistema estadoAtual) throws NadaParaRefazerException {
        if (redo.isEmpty()) {
            throw new NadaParaRefazerException();
        }
        undo.push(estadoAtual.copiar());
        return redo.pop();
    }

    void cancelarRedo(EstadoSistema estadoRefeito) {
        redo.push(estadoRefeito.copiar());
        undo.pop();
    }
}
