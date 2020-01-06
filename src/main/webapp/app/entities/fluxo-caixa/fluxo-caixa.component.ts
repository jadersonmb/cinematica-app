import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IFluxoCaixa } from 'app/shared/model/fluxo-caixa.model';
import { FluxoCaixaService } from './fluxo-caixa.service';
import { FluxoCaixaDeleteDialogComponent } from './fluxo-caixa-delete-dialog.component';

@Component({
  selector: 'jhi-fluxo-caixa',
  templateUrl: './fluxo-caixa.component.html'
})
export class FluxoCaixaComponent implements OnInit, OnDestroy {
  fluxoCaixas?: IFluxoCaixa[];
  eventSubscriber?: Subscription;
  currentSearch: string;

  constructor(
    protected fluxoCaixaService: FluxoCaixaService,
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
      this.fluxoCaixaService
        .search({
          query: this.currentSearch
        })
        .subscribe((res: HttpResponse<IFluxoCaixa[]>) => (this.fluxoCaixas = res.body ? res.body : []));
      return;
    }
    this.fluxoCaixaService.query().subscribe((res: HttpResponse<IFluxoCaixa[]>) => {
      this.fluxoCaixas = res.body ? res.body : [];
      this.currentSearch = '';
    });
  }

  search(query: string): void {
    this.currentSearch = query;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInFluxoCaixas();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IFluxoCaixa): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInFluxoCaixas(): void {
    this.eventSubscriber = this.eventManager.subscribe('fluxoCaixaListModification', () => this.loadAll());
  }

  delete(fluxoCaixa: IFluxoCaixa): void {
    const modalRef = this.modalService.open(FluxoCaixaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.fluxoCaixa = fluxoCaixa;
  }
}
