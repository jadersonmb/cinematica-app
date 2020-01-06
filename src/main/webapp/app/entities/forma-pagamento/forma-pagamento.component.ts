import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IFormaPagamento } from 'app/shared/model/forma-pagamento.model';
import { FormaPagamentoService } from './forma-pagamento.service';
import { FormaPagamentoDeleteDialogComponent } from './forma-pagamento-delete-dialog.component';

@Component({
  selector: 'jhi-forma-pagamento',
  templateUrl: './forma-pagamento.component.html'
})
export class FormaPagamentoComponent implements OnInit, OnDestroy {
  formaPagamentos?: IFormaPagamento[];
  eventSubscriber?: Subscription;
  currentSearch: string;

  constructor(
    protected formaPagamentoService: FormaPagamentoService,
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
      this.formaPagamentoService
        .search({
          query: this.currentSearch
        })
        .subscribe((res: HttpResponse<IFormaPagamento[]>) => (this.formaPagamentos = res.body ? res.body : []));
      return;
    }
    this.formaPagamentoService.query().subscribe((res: HttpResponse<IFormaPagamento[]>) => {
      this.formaPagamentos = res.body ? res.body : [];
      this.currentSearch = '';
    });
  }

  search(query: string): void {
    this.currentSearch = query;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInFormaPagamentos();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IFormaPagamento): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInFormaPagamentos(): void {
    this.eventSubscriber = this.eventManager.subscribe('formaPagamentoListModification', () => this.loadAll());
  }

  delete(formaPagamento: IFormaPagamento): void {
    const modalRef = this.modalService.open(FormaPagamentoDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.formaPagamento = formaPagamento;
  }
}
