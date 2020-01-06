import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IEmpresa } from 'app/shared/model/empresa.model';
import { EmpresaService } from './empresa.service';
import { EmpresaDeleteDialogComponent } from './empresa-delete-dialog.component';

@Component({
  selector: 'jhi-empresa',
  templateUrl: './empresa.component.html'
})
export class EmpresaComponent implements OnInit, OnDestroy {
  empresas?: IEmpresa[];
  eventSubscriber?: Subscription;
  currentSearch: string;

  constructor(
    protected empresaService: EmpresaService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected activatedRoute: ActivatedRoute
  ) {
    this.currentSearch =
      this.activatedRoute.snapshot && this.activatedRoute.snapshot.queryParams['search']
        ? this.activatedRoute.snapshot.queryParams['search']
        : '';
  }

  loadAll(): void {
    if (this.currentSearch) {
      this.empresaService
        .search({
          query: this.currentSearch
        })
        .subscribe((res: HttpResponse<IEmpresa[]>) => (this.empresas = res.body ? res.body : []));
      return;
    }
    this.empresaService.query().subscribe((res: HttpResponse<IEmpresa[]>) => {
      this.empresas = res.body ? res.body : [];
      this.currentSearch = '';
    });
  }

  search(query: string): void {
    this.currentSearch = query;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInEmpresas();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IEmpresa): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInEmpresas(): void {
    this.eventSubscriber = this.eventManager.subscribe('empresaListModification', () => this.loadAll());
  }

  delete(empresa: IEmpresa): void {
    const modalRef = this.modalService.open(EmpresaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.empresa = empresa;
  }
}
