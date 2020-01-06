import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IEmpresa, Empresa } from 'app/shared/model/empresa.model';
import { EmpresaService } from './empresa.service';
import { IEndereco } from 'app/shared/model/endereco.model';
import { EnderecoService } from 'app/entities/endereco/endereco.service';

@Component({
  selector: 'jhi-empresa-update',
  templateUrl: './empresa-update.component.html'
})
export class EmpresaUpdateComponent implements OnInit {
  isSaving = false;

  enderecos: IEndereco[] = [];

  editForm = this.fb.group({
    id: [],
    nomeFantasia: [],
    razaoSocial: [],
    cnpj: [],
    telefone: [],
    email: [],
    inscricaoEstadual: [],
    inscricaoMunicipal: [],
    website: [],
    dataContratacao: [],
    endereco: []
  });

  constructor(
    protected empresaService: EmpresaService,
    protected enderecoService: EnderecoService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ empresa }) => {
      this.updateForm(empresa);

      this.enderecoService
        .query()
        .pipe(
          map((res: HttpResponse<IEndereco[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IEndereco[]) => (this.enderecos = resBody));
    });
  }

  updateForm(empresa: IEmpresa): void {
    this.editForm.patchValue({
      id: empresa.id,
      nomeFantasia: empresa.nomeFantasia,
      razaoSocial: empresa.razaoSocial,
      cnpj: empresa.cnpj,
      telefone: empresa.telefone,
      email: empresa.email,
      inscricaoEstadual: empresa.inscricaoEstadual,
      inscricaoMunicipal: empresa.inscricaoMunicipal,
      website: empresa.website,
      dataContratacao: empresa.dataContratacao != null ? empresa.dataContratacao.format(DATE_TIME_FORMAT) : null,
      endereco: empresa.endereco
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const empresa = this.createFromForm();
    if (empresa.id !== undefined) {
      this.subscribeToSaveResponse(this.empresaService.update(empresa));
    } else {
      this.subscribeToSaveResponse(this.empresaService.create(empresa));
    }
  }

  private createFromForm(): IEmpresa {
    return {
      ...new Empresa(),
      id: this.editForm.get(['id'])!.value,
      nomeFantasia: this.editForm.get(['nomeFantasia'])!.value,
      razaoSocial: this.editForm.get(['razaoSocial'])!.value,
      cnpj: this.editForm.get(['cnpj'])!.value,
      telefone: this.editForm.get(['telefone'])!.value,
      email: this.editForm.get(['email'])!.value,
      inscricaoEstadual: this.editForm.get(['inscricaoEstadual'])!.value,
      inscricaoMunicipal: this.editForm.get(['inscricaoMunicipal'])!.value,
      website: this.editForm.get(['website'])!.value,
      dataContratacao:
        this.editForm.get(['dataContratacao'])!.value != null
          ? moment(this.editForm.get(['dataContratacao'])!.value, DATE_TIME_FORMAT)
          : undefined,
      endereco: this.editForm.get(['endereco'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmpresa>>): void {
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

  trackById(index: number, item: IEndereco): any {
    return item.id;
  }
}
