import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IFormaPagamento, FormaPagamento } from 'app/shared/model/forma-pagamento.model';
import { FormaPagamentoService } from './forma-pagamento.service';

@Component({
  selector: 'jhi-forma-pagamento-update',
  templateUrl: './forma-pagamento-update.component.html'
})
export class FormaPagamentoUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    descricao: []
  });

  constructor(protected formaPagamentoService: FormaPagamentoService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ formaPagamento }) => {
      this.updateForm(formaPagamento);
    });
  }

  updateForm(formaPagamento: IFormaPagamento): void {
    this.editForm.patchValue({
      id: formaPagamento.id,
      descricao: formaPagamento.descricao
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const formaPagamento = this.createFromForm();
    if (formaPagamento.id !== undefined) {
      this.subscribeToSaveResponse(this.formaPagamentoService.update(formaPagamento));
    } else {
      this.subscribeToSaveResponse(this.formaPagamentoService.create(formaPagamento));
    }
  }

  private createFromForm(): IFormaPagamento {
    return {
      ...new FormaPagamento(),
      id: this.editForm.get(['id'])!.value,
      descricao: this.editForm.get(['descricao'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFormaPagamento>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }
}
