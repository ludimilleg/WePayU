package br.ufal.ic.p2.wepayu;

import br.ufal.ic.p2.wepayu.Exception.NadaParaDesfazerException;
import br.ufal.ic.p2.wepayu.Exception.NadaParaRefazerException;
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

    public void descartarUltimoUndo() {
        if (!undo.isEmpty()) {
            undo.pop();
        }
    }

    public EstadoSistema undo(EstadoSistema estadoAtual) throws NadaParaDesfazerException {
        if (undo.isEmpty()) {
            throw new NadaParaDesfazerException();
        }
        redo.push(estadoAtual.copiar());
        return undo.pop();
    }

    public EstadoSistema redo(EstadoSistema estadoAtual) throws NadaParaRefazerException {
        if (redo.isEmpty()) {
            throw new NadaParaRefazerException();
        }
        undo.push(estadoAtual.copiar());
        return redo.pop();
    }
}
