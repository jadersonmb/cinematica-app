import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IProfissao, Profissao } from 'app/shared/model/profissao.model';
import { ProfissaoService } from './profissao.service';

@Component({
  selector: 'jhi-profissao-update',
  templateUrl: './profissao-update.component.html'
})
export class ProfissaoUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    descricao: []
  });

  constructor(protected profissaoService: ProfissaoService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ profissao }) => {
      this.updateForm(profissao);
    });
  }

  updateForm(profissao: IProfissao): void {
    this.editForm.patchValue({
      id: profissao.id,
      descricao: profissao.descricao
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const profissao = this.createFromForm();
    if (profissao.id !== undefined) {
      this.subscribeToSaveResponse(this.profissaoService.update(profissao));
    } else {
      this.subscribeToSaveResponse(this.profissaoService.create(profissao));
    }
  }

  private createFromForm(): IProfissao {
    return {
      ...new Profissao(),
      id: this.editForm.get(['id'])!.value,
      descricao: this.editForm.get(['descricao'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProfissao>>): void {
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
