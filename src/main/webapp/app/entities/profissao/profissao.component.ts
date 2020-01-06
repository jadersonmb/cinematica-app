import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IProfissao } from 'app/shared/model/profissao.model';
import { ProfissaoService } from './profissao.service';
import { ProfissaoDeleteDialogComponent } from './profissao-delete-dialog.component';

@Component({
  selector: 'jhi-profissao',
  templateUrl: './profissao.component.html'
})
export class ProfissaoComponent implements OnInit, OnDestroy {
  profissaos?: IProfissao[];
  eventSubscriber?: Subscription;
  currentSearch: string;

  constructor(
    protected profissaoService: ProfissaoService,
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
      this.profissaoService
        .search({
          query: this.currentSearch
        })
        .subscribe((res: HttpResponse<IProfissao[]>) => (this.profissaos = res.body ? res.body : []));
      return;
    }
    this.profissaoService.query().subscribe((res: HttpResponse<IProfissao[]>) => {
      this.profissaos = res.body ? res.body : [];
      this.currentSearch = '';
    });
  }

  search(query: string): void {
    this.currentSearch = query;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInProfissaos();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IProfissao): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInProfissaos(): void {
    this.eventSubscriber = this.eventManager.subscribe('profissaoListModification', () => this.loadAll());
  }

  delete(profissao: IProfissao): void {
    const modalRef = this.modalService.open(ProfissaoDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.profissao = profissao;
  }
}
