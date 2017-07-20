from django.db.models import Q
from django.http import HttpResponse
from django.shortcuts import render
from .models import Book, Publisher


# Create your views here.
def index(request):
    return render(request, 'index.html')


def addBook(request):
    return render(request, 'addBook.html')


def saveInfo(request):
    aTitle = request.POST['title']
    aAuthors = request.POST['authors']
    aPublisherName = request.POST['publisher']
    aPublisher = Publisher(name=aPublisherName,
                           address='',
                           city='',
                           state_province='',
                           country='',
                           website='')
    aPublication_date = request.POST['publication_date']

    Publisher.objects.create(name=aPublisherName,
                             address='',
                             city='',
                             state_province='',
                             country='',
                             website='')

    Book.objects.create(title=aTitle,
                        authors=aAuthors,
                        publisher=Publisher.objects.last(),
                        publication_date=aPublication_date)
    return render(request, 'index.html')


def showData(request):
    data = Book.objects.all()
    return render(request, 'showData.html', {'alldata': data})


def search(request):
    query = request.GET.get('q', '')
    if query:
        qset = (
            Q(title__icontains=query) |
            Q(authors__first_name__icontains=query) |
            Q(authors__last_name__icontains=query) |
            Q(publisher__name__icontains=query)
        )
        results = Book.objects.filter(qset).distinct()
    else:
        results = []

    return render(request, 'search.html', {'results': results, 'query':query})
