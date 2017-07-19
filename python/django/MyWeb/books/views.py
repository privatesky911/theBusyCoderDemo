from django.http import HttpResponse
from django.shortcuts import render
from .models import Book, Publisher

# Create your views here.
def index(request):
    return render(request,'index.html')


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
    return render(request, 'showData.html', {'alldata':data})