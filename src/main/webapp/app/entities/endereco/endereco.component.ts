import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IEndereco } from 'app/shared/model/endereco.model';
import { EnderecoService } from './endereco.service';
import { EnderecoDeleteDialogComponent } from './endereco-delete-dialog.component';

@Component({
  selector: 'jhi-endereco',
  templateUrl: './endereco.component.html'
})
export class EnderecoComponent implements OnInit, OnDestroy {
  enderecos?: IEndereco[];
  eventSubscriber?: Subscription;
  currentSearch: string;

  constructor(
    protected enderecoService: EnderecoService,
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
      this.enderecoService
        .search({
          query: this.currentSearch
        })
        .subscribe((res: HttpResponse<IEndereco[]>) => (this.enderecos = res.body ? res.body : []));
      return;
    }
    this.enderecoService.query().subscribe((res: HttpResponse<IEndereco[]>) => {
      this.enderecos = res.body ? res.body : [];
      this.currentSearch = '';
    });
  }

  search(query: string): void {
    this.currentSearch = query;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInEnderecos();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IEndereco): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInEnderecos(): void {
    this.eventSubscriber = this.eventManager.subscribe('enderecoListModification', () => this.loadAll());
  }

  delete(endereco: IEndereco): void {
    const modalRef = this.modalService.open(EnderecoDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.endereco = endereco;
  }
}
