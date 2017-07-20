from django.contrib import admin

# Register your models here.
from books.models import Book, Publisher, Author

admin.site.register(Book)
admin.site.register(Publisher)
admin.site.register(Author)