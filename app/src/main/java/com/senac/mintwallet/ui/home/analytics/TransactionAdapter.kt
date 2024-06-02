package com.senac.mintwallet.ui.home.analytics


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.senac.mintwallet.R
import com.senac.mintwallet.databinding.TransactionItemBinding
import com.senac.mintwallet.model.ETypeTransfer
import com.senac.mintwallet.model.TransferEntity
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TransactionAdapter(private val transactionItems: List<Transfer>) :
    RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    class TransactionViewHolder(private val binding: TransactionItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(tranf: Transfer) {
            val apply = binding.apply {
                transactionTime.text = extractTimeFromString(tranf.createdAt)
                transactionName.text = tranf.describe
                """R$ ${tranf.amount}""".also { transactionAmount.text = it }

                var icon = R.drawable.payment
                transactionType.text = "Transferencia"

                if(tranf.typeTransfer == ETypeTransfer.PAYMENT.toString()) {
                    icon = R.drawable.transfer
                    transactionType.text = "Pagamento"
                }

                transactionIcon.setImageResource(icon)

            }
        }

        private fun extractTimeFromString(dateString: String): String {
            val inputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
            val outputFormatter = DateTimeFormatter.ofPattern("dd/MM HH:mm")

            val dateTime = LocalDateTime.parse(dateString, inputFormatter)
            return dateTime.format(outputFormatter)
        }

        class ItemDiff : DiffUtil.ItemCallback<TransferEntity>() {
            override fun areItemsTheSame(oldItem: TransferEntity, newItem: TransferEntity): Boolean {
                return oldItem.uuid == newItem.uuid
            }

            override fun areContentsTheSame(
                oldItem: TransferEntity,
                newItem: TransferEntity
            ): Boolean {
                return oldItem.uuid == newItem.uuid &&
                        oldItem.describe == newItem.describe
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val binding = TransactionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TransactionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactionItems[position]
        holder.bind(transaction)
    }

    override fun getItemCount() = transactionItems.size
}